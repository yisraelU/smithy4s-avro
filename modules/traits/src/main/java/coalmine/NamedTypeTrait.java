package coalmine;

import software.amazon.smithy.model.SourceLocation;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.StringTrait;

public class NamedTypeTrait  extends StringTrait {

    public static final ShapeId ID = ShapeId.from("coalmine#avroNamedType");

    public NamedTypeTrait(String value, SourceLocation sourceLocation) {
        super(ID, value, sourceLocation);
    }

    public NamedTypeTrait(String value) {
        this(value, SourceLocation.NONE);
    }

    public static final class Provider extends StringTrait.Provider<NamedTypeTrait> {
        public Provider() {
            super(ID, NamedTypeTrait::new);
        }
    }
}
