package coalmine;

import software.amazon.smithy.model.FromSourceLocation;
import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AbstractTrait;

public class AvroTime extends AbstractTrait {

    enum Format {
        DATE,
        TIME_MILLIS,
        TIME_MICROS,
        TIMESTAMP_MILLIS,
        TIMESTAMP_MICROS,
        LOCAL_TIMESTAMP_MILLIS,
        LOCAL_TIMESTAMP_MICROS,
    }
    public AvroTime(ShapeId id, FromSourceLocation sourceLocation) {
        super(id, sourceLocation);
    }

    @Override
    protected Node createNode() {
        return null;
    }
}
