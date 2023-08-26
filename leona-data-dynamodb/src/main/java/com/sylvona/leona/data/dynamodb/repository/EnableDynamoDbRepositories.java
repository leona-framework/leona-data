package com.sylvona.leona.data.dynamodb.repository;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.query.QueryLookupStrategy;

import java.lang.annotation.*;

/**
 * Annotation to enable DynamoDB repositories. Will scan the package of the
 * annotated configuration class for Spring Data repositories by default.
 *
 * @author Michael Lavelle
 * @author Sebastian Just
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(DynamoDbRepositoriesRegistrar.class)
public @interface EnableDynamoDbRepositories {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise
     * annotation declarations e.g.:
     * {@code @EnableDynamoDBRepositories("org.my.pkg")} instead of
     * {@code @EnableDynamoDBaRepositories(basePackages="org.my.pkg")}.
     *
     * @return The package name for scanning
     */
    String[] value() default {};

    /**
     * Base packages to scan for annotated components. {@link #value()} is an alias
     * for (and mutually exclusive with) this attribute. Use
     * {@link #basePackageClasses()} for a type-safe alternative to String-based
     * package names.
     *
     * @return The package name for scanning
     */
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages
     * to scan for annotated components. The package of each class specified will be
     * scanned. Consider creating a special no-op marker class or interface in each
     * package that serves no purpose other than being referenced by this attribute.
     *
     * @return The class to figure out the base package for scanning
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * Specifies which types are eligible for component scanning. Further narrows
     * the set of candidate components from everything in {@link #basePackages()} to
     * everything in the base packages that matches the given filter or filters.
     *
     * @return All the include filters
     */
    ComponentScan.Filter[] includeFilters() default {};

    /**
     * Specifies which types are not eligible for component scanning.
     *
     * @return All the exclude filters
     */
    ComponentScan.Filter[] excludeFilters() default {};

    /**
     * Returns the postfix to be used when looking up custom repository
     * implementations. Defaults to {@literal Impl}. So for a repository named
     * {@code PersonRepository} the corresponding implementation class will be
     * looked up scanning for {@code PersonRepositoryImpl}. Defaults to 'Impl'.
     *
     * @return The implementation postfix that's used
     */
    String repositoryImplementationPostfix() default "Impl";

    /**
     * Configures the location of where to find the Spring Data named queries
     * properties file. Will default to
     * {@code META-INFO/jpa-named-queries.properties}.
     *
     * @return The location itself
     */
    String namedQueriesLocation() default "";

    /**
     * Returns the key of the
     * {@link org.springframework.data.repository.query.QueryLookupStrategy} to be
     * used for lookup queries for query methods. Defaults to
     * {@link QueryLookupStrategy.Key#CREATE_IF_NOT_FOUND}.
     *
     * @return The lookup strategy
     */
    QueryLookupStrategy.Key queryLookupStrategy() default QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND;

    /**
     * Returns the {@link org.springframework.beans.factory.FactoryBean} class to be
     * used for each repository instance. Defaults to
     * {@link DynamoRepositoryFactoryBeanSupport}.
     *
     * @return The repository factory bean cleass
     */
    Class<?> repositoryFactoryBeanClass() default DynamoRepositoryFactoryBeanSupport.class;
}
