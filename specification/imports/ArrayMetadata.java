/**
 * Represents details of an array in a field/property in a class.
 * @since 2.3
 */
public interface ArrayMetadata extends Metadata {
    /**
     * Method to set the name of the element type
     * 
     * @param type Name of the element type
     */
    ArrayMetadata setElementType(String type);

    /**
     * Accessor for the element type
     * 
     * @return The element type
     */
    String getElementType();

    /**
     * Method to set whether the element is embedded
     * 
     * @param val Whether it is embedded
     */
    ArrayMetadata setEmbeddedElement(boolean val);

    /**
     * Accessor for whether the element is embedded
     * 
     * @return whether the element is embedded
     */
    Boolean getEmbeddedElement();

    /**
     * Method to set whether the element is serialised
     * 
     * @param val Whether it is serialised
     */
    ArrayMetadata setSerializedElement(boolean val);

    /**
     * Accessor for whether the element is serialised
     * 
     * @return whether the element is serialised
     */
    Boolean getSerializedElement();

    /**
     * Method to set whether the element is dependent
     * 
     * @param val Whether it is dependent
     */
    ArrayMetadata setDependentElement(boolean val);

    /**
     * Accessor for whether the element is dependent
     * 
     * @return whether the element is dependent
     */
    Boolean getDependentElement();
}
