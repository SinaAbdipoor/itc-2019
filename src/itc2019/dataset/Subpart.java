package itc2019.dataset;

/**
 * This class represents subparts as defined in the ITC 2019.
 */
record Subpart(int id, Class[] classes) {
    /**
     * Constructs a Subpart object.
     *
     * @param id      The id of the subpart.
     * @param classes The list of classes in this subpart.
     * @throws IllegalArgumentException If the passed id is invalid.
     */
    Subpart {
        if (id < 1) throw new IllegalArgumentException("Room id cannot be less than 1!");
    }
}