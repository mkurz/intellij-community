package org.jetbrains.jet.lang.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author abreslav
 */
public class TupleType extends TypeImpl {

    public static final TupleType UNIT = new TupleType(Collections.<Attribute>emptyList(), Collections.<Type>emptyList());

    public static TupleType getTupleType(List<Attribute> attributes, List<Type> arguments) {
        if (attributes.isEmpty() && arguments.isEmpty()) {
            return UNIT;
        }
        return new TupleType(attributes, arguments);
    }


    public static TupleType getTupleType(List<Type> arguments) {
        return getTupleType(Collections.<Attribute>emptyList(), arguments);
    }

    public static TupleType getLabeledTupleType(List<Attribute> attributes, List<ParameterDescriptor> arguments) {
        return getTupleType(attributes, toTypes(arguments));
    }


    public static TupleType getLabeledTupleType(List<ParameterDescriptor> arguments) {
        return getLabeledTupleType(Collections.<Attribute>emptyList(), arguments);
    }


    private TupleType(List<Attribute> attributes, List<Type> arguments) {
        super(attributes, JetStandardClasses.getTuple(arguments.size()).getTypeConstructor(), toProjections(arguments));
    }

    @Override
    public Collection<MemberDescriptor> getMembers() {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    @Override
    public <R, D> R accept(TypeVisitor<R, D> visitor, D data) {
        return visitor.visitTupleType(this, data);
    }

    private static List<TypeProjection> toProjections(List<Type> arguments) {
        List<TypeProjection> result = new ArrayList<TypeProjection>();
        for (Type argument : arguments) {
            result.add(new TypeProjection(ProjectionKind.OUT_ONLY, argument));
        }
        return result;
    }

    private static List<Type> toTypes(List<ParameterDescriptor> labeledEntries) {
        List<Type> result = new ArrayList<Type>();
        for (ParameterDescriptor entry : labeledEntries) {
            result.add(entry.getType());
        }
        return result;
    }
}
