package parser.utils;

import scala.collection.JavaConverters;

import java.util.List;

/**
 * Factory for creating scala-made Specification objects
 */
abstract public class SpecificationJava {

    /**
     * Static factory method for creating different POSTag instances
     * This method overload represents the users decision to not pass an additional Boolean flag
     *
     * @param tag - The specific POSTag to be created
     * @return a POSTag element with information regarding if its values should be an array or not
     */
    public static POSTag postTag(POSTagEnum tag) {
        return postTag(tag, false);
    }

    /**
     * Static factory method for creating different POSTag instances
     *
     * @param tag        - The specific POSTag to be created
     * @param isMultiple - Boolean flag saying if the given keyword should have an array of values or not
     * @return a POSTag element with information regarding if its values should be an array or not
     */
    public static POSTag postTag(POSTagEnum tag, Boolean isMultiple) {
        switch (tag) {
            case ADJECTIVE:
                return new Adjective(isMultiple);

            case PROPERNOUN:
                return new ProperNoun(isMultiple);

            case NOUN:
                return new Noun(isMultiple);

            case PLURALNOUN:
                return new PluralNoun(isMultiple);

            case VERB:
                return new Verb(isMultiple);

            case VERBPASTPARTICIPLE:
                return new VerbPastParticiple(isMultiple);

            case VERBGERUND:
                return new VerbGerund(isMultiple);

            case NUMBER:
                return new Number(isMultiple);

            case ADVERB:
                return new Adverb(isMultiple);

            default:
                return null; //Unreachle statement
        }
    }

    /**
     * Static factory method that returns an instance of class OneOf.
     * This method overload represents the user decision to not pass in an aditional Booleean flag.
     *
     * @param possibilities - Possible values list to find only one of them when parsing some text
     * @return an instance of class OneOf with the correct possibilities list
     */
    public static OneOf oneOf(List<String> possibilities) {
        return oneOf(possibilities, false);
    }

    /**
     * Static factory method that returns an instance of class OneOf
     *
     * @param possibilities - Possible values list to find only one of them when parsing some text
     * @param isMultiple    - Boolean flag specifying if this parameter should be an array or not
     * @return an instance of class OneOf with the correct possibilities list
     */
    public static OneOf oneOf(List<String> possibilities, Boolean isMultiple) {
        return new OneOf(toScalaList(possibilities), isMultiple);
    }

    /**
     * Static factory method that returns an instance of MultipleOf
     * This method overload represents the user decision to not pass in an aditional Booleean flag.
     *
     * @param possibilities - Possible values list to find one or more of them when parsing some text
     * @return an instance of class OneOf with the correct possibilities list
     */
    public static MultipleOf multipleOf(List<String> possibilities) {
        return new MultipleOf(toScalaList(possibilities), false);
    }

    /**
     * Static factory method that returns an instance of MultipleOf
     *
     * @param possibilities - Possible values list to find one or more of them when parsing some text
     * @return an instance of class OneOf with the correct possibilities list
     */
    public static MultipleOf multipleOf(List<String> possibilities, Boolean isMultiple) {
        return new MultipleOf(toScalaList(possibilities), isMultiple);
    }

    /**
     * Auxaliary method used to convert a java list to an scala immutable list
     *
     * @param javaList - java.util.List to be converted to a scala.collection.immutable.List
     * @return a scala immutable list converted from the inputed java list
     */
    private static scala.collection.immutable.List<String> toScalaList(List<String> javaList) {
        //Convert Java List<String> to a scala mutable Buffer
        scala.collection.mutable.Buffer<String> scalaBuffer = JavaConverters.asScalaBuffer(javaList);

        //Convert scala mutable Buffer to a scala immutable List
        return scalaBuffer.toList();
    }
}
