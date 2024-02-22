package coalmine;

import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.node.ObjectNode;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AnnotationTrait;
import software.amazon.smithy.model.traits.AbstractTrait;

public class AvroSchema extends AnnotationTrait {

    public static ShapeId ID = ShapeId.from("coalmine.avro#avroSchema");

    public AvroSchema(ObjectNode node) {
        super(ID, node);
    }

    public AvroSchema() {
        super(ID, Node.objectNode());
    }

    public static final class Provider extends AbstractTrait.Provider {
        public Provider() {
            super(ID);
        }

        @Override
        public AvroSchema createTrait(ShapeId target, Node node) {
            return new AvroSchema(node.expectObjectNode());
        }
    }
}

