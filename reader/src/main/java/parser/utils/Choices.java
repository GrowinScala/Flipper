//package parser.utils;
//
//import scala.collection.JavaConverters;
//
//import java.util.List;
//
///**
// * Factory for creating scala-made OneOf and MultipleOf objects
// */
//abstract public class Choices {
//
//    /**
//     * Static factory method that returns an instance of class OneOf
//     *
//     * @param possibilities - Possible values list to find only one of them when parsing some text
//     * @return an instance of class OneOf with the correct possibilities list
//     */
//    public static OneOf oneOf(List<String> possibilities) {
//        return new OneOf(toScalaList(possibilities));
//    }
//
//    /**
//     * Static factory method that returns an instance of MultipleOf
//     *
//     * @param possibilities - Possible values list to find one or more of them when parsing some text
//     * @return an instance of class OneOf with the correct possibilities list
//     */
//    public static MultipleOf multipleOf(List<String> possibilities) {
//        return new MultipleOf(false, toScalaList(possibilities));
//    }
//
//    /**
//     * Auxaliary method used to convert a java list to an scala immutable list
//     *
//     * @param javaList - java.util.List to be converted to a scala.collection.immutable.List
//     * @return a scala immutable list converted from the inputed java list
//     */
//    private static scala.collection.immutable.List<String> toScalaList(List<String> javaList) {
//        //Convert Java List<String> to a scala mutable Buffer
//        scala.collection.mutable.Buffer<String> scalaBuffer = JavaConverters.asScalaBuffer(javaList);
//
//        //Convert scala mutable Buffer to a scala immutable List
//        return scalaBuffer.toList();
//    }
//}
