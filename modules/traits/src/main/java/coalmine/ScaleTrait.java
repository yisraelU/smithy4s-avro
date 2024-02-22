package coalmine;

import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AnnotationTrait;

public class ScaleTrait extends AnnotationTrait {
    public static ShapeId ID = ShapeId.from("coalmine.avro#scale");

    public ScaleTrait() {
        super(ID, Node.objectNode());
    }

    public static final class Provider extends AnnotationTrait.Provider<coalmine.ScaleTrait> {
        public Provider() {
            super(ID, (node) -> new coalmine.ScaleTrait());
        }
    }

}
