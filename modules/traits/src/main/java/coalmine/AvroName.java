package coalmine;

import software.amazon.smithy.model.SourceLocation;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.StringTrait;

public class AvroName extends StringTrait {

    public static final ShapeId ID = ShapeId.from("coalmine#avroName");

    public AvroName(String value, SourceLocation sourceLocation) {
        super(ID, value, sourceLocation);
    }

    public AvroName(String value) {
        this(value, SourceLocation.NONE);
    }

    public static final class Provider extends StringTrait.Provider<AvroName> {
        public Provider() {
            super(ID, AvroName::new);
        }
    }
}
