package io;

import dataset.*;
import dataset.Class;
import dataset.constraints.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

/**
 * Utility class to load a ProblemInstance from an ITC 2019 XML file.
 */
public class Parser {

    /**
     * Loads and parses an ITC 2019 XML file, returning a ProblemInstance.
     * @param xmlFile The XML file to parse.
     * @return The parsed ProblemInstance.
     * @throws Exception If parsing fails.
     */
    public static ProblemInstance loadFromXML(File xmlFile) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        // Parse root attributes
        String instanceName = root.getAttribute("name");
        int nrDays = Integer.parseInt(root.getAttribute("nrDays"));
        int nrWeeks = Integer.parseInt(root.getAttribute("nrWeeks"));
        int slotsPerDay = Integer.parseInt(root.getAttribute("slotsPerDay"));

        // Parse optimization weights
        Element optElem = (Element) root.getElementsByTagName("optimization").item(0);
        int timePenaltyWeight = Integer.parseInt(optElem.getAttribute("time"));
        int roomPenaltyWeight = Integer.parseInt(optElem.getAttribute("room"));
        int distributionPenaltyWeight = Integer.parseInt(optElem.getAttribute("distribution"));
        int studentPenaltyWeight = Integer.parseInt(optElem.getAttribute("student"));

        // --- Parse Rooms ---
        List<Room> roomList = new ArrayList<>();
        Map<Integer, Room> roomMap = new HashMap<>();
        List<int[]> travelEntries = new ArrayList<>(); // [fromId, toId, value]
        NodeList roomNodes = ((Element) root.getElementsByTagName("rooms").item(0)).getElementsByTagName("room");
        for (int i = 0; i < roomNodes.getLength(); i++) {
            Element roomElem = (Element) roomNodes.item(i);
            int roomId = Integer.parseInt(roomElem.getAttribute("id"));
            int capacity = Integer.parseInt(roomElem.getAttribute("capacity"));

            // Parse unavailable times
            List<Time> unavailableTimes = new ArrayList<>();
            NodeList unavailableNodes = roomElem.getElementsByTagName("unavailable");
            for (int j = 0; j < unavailableNodes.getLength(); j++) {
                Element unElem = (Element) unavailableNodes.item(j);
                unavailableTimes.add(parseTime(unElem, nrWeeks, nrDays));
            }

            // Parse travel times
            NodeList travelNodes = roomElem.getElementsByTagName("travel");
            for (int j = 0; j < travelNodes.getLength(); j++) {
                Element travelElem = (Element) travelNodes.item(j);
                int toId = Integer.parseInt(travelElem.getAttribute("room"));
                int value = Integer.parseInt(travelElem.getAttribute("value"));
                travelEntries.add(new int[]{roomId, toId, value});
            }

            Room room = new Room(roomId, capacity, unavailableTimes.toArray(new Time[0]));
            roomList.add(room);
            roomMap.put(roomId, room);
        }

        // Build TravelTime matrix
        TravelTime travelTime = TravelTime.createInstance(roomList.size());
        for (int[] entry : travelEntries) {
            Room from = roomMap.get(entry[0]);
            Room to = roomMap.get(entry[1]);
            travelTime.setTravelTime(from, to, entry[2]);
            travelTime.setTravelTime(to, from, entry[2]); // symmetric
        }

        // --- Parse Courses ---
        List<Course> courseList = new ArrayList<>();
        Map<Integer, Course> courseMap = new HashMap<>();
        Map<Integer, Class> classMap = new HashMap<>();
        NodeList courseNodes = ((Element) root.getElementsByTagName("courses").item(0)).getElementsByTagName("course");
        for (int i = 0; i < courseNodes.getLength(); i++) {
            Element courseElem = (Element) courseNodes.item(i);
            int courseId = Integer.parseInt(courseElem.getAttribute("id"));
            List<Config> configList = new ArrayList<>();

            NodeList configNodes = courseElem.getElementsByTagName("config");
            for (int j = 0; j < configNodes.getLength(); j++) {
                Element configElem = (Element) configNodes.item(j);
                int configId = Integer.parseInt(configElem.getAttribute("id"));
                List<Subpart> subpartList = new ArrayList<>();

                NodeList subpartNodes = configElem.getElementsByTagName("subpart");
                for (int k = 0; k < subpartNodes.getLength(); k++) {
                    Element subpartElem = (Element) subpartNodes.item(k);
                    int subpartId = Integer.parseInt(subpartElem.getAttribute("id"));
                    List<Class> subpartClasses = new ArrayList<>();

                    NodeList classNodes = subpartElem.getElementsByTagName("class");
                    for (int l = 0; l < classNodes.getLength(); l++) {
                        Element classElem = (Element) classNodes.item(l);
                        int classId = Integer.parseInt(classElem.getAttribute("id"));
                        int limit = Integer.parseInt(classElem.getAttribute("limit"));
                        boolean needRoom = !"false".equals(classElem.getAttribute("room"));

                        // Parent class
                        Class parent = null;
                        if (classElem.hasAttribute("parent")) {
                            int parentId = Integer.parseInt(classElem.getAttribute("parent"));
                            parent = classMap.get(parentId); // Will be set after all classes are parsed
                        }

                        // Parse possible rooms
                        List<RoomAssignment> possibleRooms = new ArrayList<>();
                        NodeList roomAssignNodes = classElem.getElementsByTagName("room");
                        for (int m = 0; m < roomAssignNodes.getLength(); m++) {
                            Element roomAssignElem = (Element) roomAssignNodes.item(m);
                            int rId = Integer.parseInt(roomAssignElem.getAttribute("id"));
                            int penalty = Integer.parseInt(roomAssignElem.getAttribute("penalty"));
                            possibleRooms.add(new RoomAssignment(roomMap.get(rId), penalty));
                        }

                        // Parse possible times
                        List<TimeAssignment> possibleTimes = new ArrayList<>();
                        NodeList timeAssignNodes = classElem.getElementsByTagName("time");
                        for (int m = 0; m < timeAssignNodes.getLength(); m++) {
                            Element timeAssignElem = (Element) timeAssignNodes.item(m);
                            Time time = parseTime(timeAssignElem, nrWeeks, nrDays);
                            int penalty = Integer.parseInt(timeAssignElem.getAttribute("penalty"));
                            possibleTimes.add(new TimeAssignment(time, penalty));
                        }

                        Class clazz = new Class(
                                classId,
                                limit,
                                possibleTimes.toArray(new TimeAssignment[0]),
                                needRoom ? possibleRooms.toArray(new RoomAssignment[0]) : null,
                                parent
                        );
                        subpartClasses.add(clazz);
                        classMap.put(classId, clazz);
                    }
                    subpartList.add(new Subpart(subpartId, subpartClasses.toArray(new Class[0])));
                }
                configList.add(new Config(configId, subpartList.toArray(new Subpart[0])));
            }
            Course course = new Course(courseId, configList.toArray(new Config[0]));
            courseList.add(course);
            courseMap.put(courseId, course);
        }

        // --- Fix parent references for classes ---
        for (Class clazz : classMap.values()) {
            if (clazz.parent() != null && clazz.parent().id() != clazz.parent().id()) {
                clazz = new Class(clazz.id(), clazz.limit(), clazz.possibleTimes(), clazz.possibleRooms(), classMap.get(clazz.parent().id()));
                classMap.put(clazz.id(), clazz);
            }
        }

        // --- Parse Students ---
        List<Student> studentList = new ArrayList<>();
        NodeList studentNodes = ((Element) root.getElementsByTagName("students").item(0)).getElementsByTagName("student");
        for (int i = 0; i < studentNodes.getLength(); i++) {
            Element studentElem = (Element) studentNodes.item(i);
            int studentId = Integer.parseInt(studentElem.getAttribute("id"));
            List<Course> studentCourses = new ArrayList<>();
            NodeList courseNodesForStudent = studentElem.getElementsByTagName("course");
            for (int j = 0; j < courseNodesForStudent.getLength(); j++) {
                Element courseElem = (Element) courseNodesForStudent.item(j);
                int cId = Integer.parseInt(courseElem.getAttribute("id"));
                studentCourses.add(courseMap.get(cId));
            }
            studentList.add(new Student(studentId, studentCourses.toArray(new Course[0])));
        }

        // --- Parse Distribution Constraints ---
        List<HardConstraint> hardConstraints = new ArrayList<>();
        List<SoftConstraint> softConstraints = new ArrayList<>();
        NodeList distNodes = ((Element) root.getElementsByTagName("distributions").item(0)).getElementsByTagName("distribution");
        for (int i = 0; i < distNodes.getLength(); i++) {
            Element distElem = (Element) distNodes.item(i);
            String type = distElem.getAttribute("type");
            boolean required = distElem.hasAttribute("required");
            int penalty = distElem.hasAttribute("penalty") ? Integer.parseInt(distElem.getAttribute("penalty")) : 0;

            List<Class> distClasses = new ArrayList<>();
            NodeList distClassNodes = distElem.getElementsByTagName("class");
            for (int j = 0; j < distClassNodes.getLength(); j++) {
                Element distClassElem = (Element) distClassNodes.item(j);
                int classId = Integer.parseInt(distClassElem.getAttribute("id"));
                distClasses.add(classMap.get(classId));
            }

            DistributionConstraint constraint = buildDistributionConstraint(type, distClasses.toArray(new Class[0]));
            if (required) {
                hardConstraints.add(new HardConstraint(constraint));
            } else {
                softConstraints.add(new SoftConstraint(constraint, penalty));
            }
        }

        // --- Build and return ProblemInstance ---
        return new ProblemInstance(
                instanceName,
                nrDays,
                nrWeeks,
                slotsPerDay,
                timePenaltyWeight,
                roomPenaltyWeight,
                distributionPenaltyWeight,
                studentPenaltyWeight,
                roomList.toArray(new Room[0]),
                courseList.toArray(new Course[0]),
                hardConstraints.toArray(new HardConstraint[0]),
                softConstraints.toArray(new SoftConstraint[0]),
                studentList.toArray(new Student[0]),
                travelTime
        );
    }

    /**
     * Parses a <time> or <unavailable> element into a Time object.
     */
    private static Time parseTime(Element elem, int nrWeeks, int nrDays) {
        String daysStr = elem.getAttribute("days");
        boolean[] days = parseBinaryString(daysStr);
        int start = Integer.parseInt(elem.getAttribute("start"));
        int length = Integer.parseInt(elem.getAttribute("length"));
        String weeksStr = elem.getAttribute("weeks");
        boolean[] weeks = parseBinaryString(weeksStr);
        return new Time(nrWeeks, nrDays, weeks, days, start, length);
    }

    /**
     * Parses a binary string (e.g., "1010100") into a boolean array.
     */
    private static boolean[] parseBinaryString(String str) {
        boolean[] arr = new boolean[str.length()];
        for (int i = 0; i < str.length(); i++) {
            arr[i] = str.charAt(i) == '1';
        }
        return arr;
    }

    /**
     * Builds a DistributionConstraint from type and classes.
     */
    private static DistributionConstraint buildDistributionConstraint(String type, Class[] classes) {
        // Handle parameterized constraints
        if (type.contains("(") && type.contains(")")) {
            String baseType = type.substring(0, type.indexOf("("));
            String paramsStr = type.substring(type.indexOf("(") + 1, type.indexOf(")"));
            String[] params = paramsStr.split(",");
            int[] intParams = Arrays.stream(params).map(String::trim).mapToInt(Integer::parseInt).toArray();

            return switch (baseType) {
                case "WorkDay" -> new WorkDay(classes, intParams[0]);
                case "MinGap" -> new MinGap(classes, intParams[0]);
                case "MaxDays" -> new MaxDays(classes, intParams[0]);
                case "MaxDayLoad" -> new MaxDayLoad(classes, intParams[0]);
                case "MaxBreaks" -> new MaxBreaks(classes, intParams[0], intParams[1]);
                case "MaxBlock" -> new MaxBlock(classes, intParams[0], intParams[1]);
                default -> throw new IllegalArgumentException("Unknown distribution type: " + type);
            };
        }
        // Non-parameterized constraints
        return switch (type) {
            case "DifferentDays" -> new DifferentDays(classes);
            case "DifferentRoom" -> new DifferentRoom(classes);
            case "DifferentTime" -> new DifferentTime(classes);
            case "DifferentWeeks" -> new DifferentWeeks(classes);
            case "SameStart" -> new SameStart(classes);
            case "SameTime" -> new SameTime(classes);
            case "SameDays" -> new SameDays(classes);
            case "SameWeeks" -> new SameWeeks(classes);
            case "Overlap" -> new Overlap(classes);
            case "NotOverlap" -> new NotOverlap(classes);
            case "SameRoom" -> new SameRoom(classes);
            case "SameAttendees" -> new SameAttendees(classes);
            case "Precedence" -> new Precedence(classes);
            default -> throw new IllegalArgumentException("Unknown distribution type: " + type);
        };
    }
}