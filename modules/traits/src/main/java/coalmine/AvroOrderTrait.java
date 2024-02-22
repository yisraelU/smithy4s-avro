package coalmine;

import software.amazon.smithy.model.FromSourceLocation;
import software.amazon.smithy.model.SourceException;
import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AbstractTrait;
import software.amazon.smithy.model.traits.AbstractTraitBuilder;
import software.amazon.smithy.model.traits.Trait;
import software.amazon.smithy.utils.ToSmithyBuilder;

public class AvroOrderTrait extends AbstractTrait implements ToSmithyBuilder<AvroOrderTrait> {

    public static ShapeId ID = ShapeId.from("avro#avroOrder");



    private final Order order;

    public Order getOrder() {
        return order;
    }

    enum Order {
        ASCENDING,
        DESCENDING,
        IGNORE
    }

    public AvroOrderTrait(AvroOrderTrait.Builder builder) {
        super(ID, builder.getSourceLocation());
        this.order = builder.order;
    }

    public static AvroOrderTrait.Builder builder() {
        return new AvroOrderTrait.Builder();
    }

    @Override
    public  AvroOrderTrait.Builder toBuilder() {
        return new AvroOrderTrait.Builder().sourceLocation(getSourceLocation()).order(order);
    }


    public static final class Builder extends AbstractTraitBuilder<AvroOrderTrait, AvroOrderTrait.Builder> {

        private Order order;

        public Builder order(Order order) {
            this.order = order;
            return this;
        }


        @Override
        public AvroOrderTrait build() {
            return new AvroOrderTrait( this);
        }
    }

    @Override
    protected Node createNode() {
        return   Node.objectNodeBuilder()
                .sourceLocation(getSourceLocation())
                .withMember("order", Node.from(order.toString()))
                .build();
    }

    public static final class Provider extends AbstractTrait.Provider {
        public Provider() {
            super(ID);
        }

        @Override
        public Trait createTrait(ShapeId target, Node value) {
            AvroOrderTrait.Builder builder = builder().sourceLocation(value);

            value.expectObjectNode()
                    .expectStringMember("order", (orderType) -> {
                        switch(orderType.toLowerCase())
                        {
                            case "ascending":
                                builder.order(Order.ASCENDING);
                                break;
                            case "descending":
                                builder.order(Order.DESCENDING);
                                break;
                            case "ignore":
                                builder.order(Order.IGNORE);
                                break;
                            default:
                                throw new SourceException("order must be either ascending, descending or ignore", value.getSourceLocation());
                        }
                    });
            return builder.build();
        }
    }
}
