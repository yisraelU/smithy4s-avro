package coalmine;

import software.amazon.smithy.model.SourceLocation;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.StringTrait;

public class AvroNamespace extends StringTrait {
    public static final ShapeId ID = ShapeId.from("coalmine#avroNamespace");

    public AvroNamespace(String value, SourceLocation sourceLocation) {
        super(ID, value, sourceLocation);
    }

    public AvroNamespace(String value) {
        this(value, SourceLocation.NONE);
    }

    public static final class Provider extends StringTrait.Provider<AvroNamespace> {
        public Provider() {
            super(ID, AvroNamespace::new);
        }
    }
}
