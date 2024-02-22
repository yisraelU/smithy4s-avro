package coalmine;

import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.node.ObjectNode;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AnnotationTrait;
import software.amazon.smithy.model.traits.AbstractTrait;

public class AvroEnabled extends AnnotationTrait {

    public static ShapeId ID = ShapeId.from("coalmine.avro#avroEnabled");

    public AvroEnabled(ObjectNode node) {
        super(ID, node);
    }

    public AvroEnabled() {
        super(ID, Node.objectNode());
    }

    public static final class Provider extends AbstractTrait.Provider {
        public Provider() {
            super(ID);
        }

        @Override
        public AvroEnabled createTrait(ShapeId target, Node node) {
            return new AvroEnabled(node.expectObjectNode());
        }
    }
}

