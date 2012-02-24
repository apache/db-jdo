@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistenceCapable
{
    /** Member declarations. Annotations for persistent members of this
     * class or interface can be specifed either here or on each member.
     * Annotations for inherited members can only be specified here.
     * @return member declarations
     */
    Persistent[] members() default {};

    /** Table to use for persisting this class or interface. 
     */
    String table() default "";

    /** Catalog to use for persisting this class or interface. 
     */
    String catalog() default "";

    /** Schema to use for persisting this class or interface. 
     */
    String schema() default "";

    /** Whether this class or interface manages an extent. 
     */
    String requiresExtent() default "";

    /** Whether objects of this class or interface can only be embedded. 
     */
    String embeddedOnly() default "";

    /** Whether this class or interface is detachable. 
     */
    String detachable() default "";

    /** Type of identity for this class or interface. 
     */
    IdentityType identityType() default IdentityType.UNSPECIFIED;

    /** Primary key class when using application identity and using own PK. 
     */
    Class objectIdClass() default void.class;

    /** Whether this class is cacheable in a Level2 cache.
     * @since 2.2
     */
    String cacheable() default "true";

    /** Any vendor extensions.
     */
    Extension[] extensions() default {};
}
