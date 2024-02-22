package coalmine;

import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.node.ObjectNode;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AnnotationTrait;
import software.amazon.smithy.model.traits.AbstractTrait;

public class RpcAvro extends AnnotationTrait {

    public static ShapeId ID = ShapeId.from("coalmine.avro#rpcAvro");

    public RpcAvro(ObjectNode node) {
        super(ID, node);
    }

    public RpcAvro() {
        super(ID, Node.objectNode());
    }

    public static final class Provider extends AbstractTrait.Provider {
        public Provider() {
            super(ID);
        }

        @Override
        public RpcAvro createTrait(ShapeId target, Node node) {
            return new RpcAvro(node.expectObjectNode());
        }
    }
}

