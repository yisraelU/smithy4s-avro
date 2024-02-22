package coalmine;

import software.amazon.smithy.model.SourceException;
import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AbstractTrait;
import software.amazon.smithy.model.traits.AbstractTraitBuilder;
import software.amazon.smithy.model.traits.Trait;
import software.amazon.smithy.utils.ToSmithyBuilder;

import java.util.Objects;

public class AvroDecimalTrait extends AbstractTrait implements ToSmithyBuilder<AvroDecimalTrait> {


    public static ShapeId ID = ShapeId.from("coalmine.avro#avroDecimal");

    // must be 0 or greater
    private final Integer precision;
    private final Integer scale;

    private final UNDERLYING_TYPE underlyingType;

    @Override
    protected Node createNode() {
        return Node.objectNodeBuilder()
                .sourceLocation(getSourceLocation())
                .withMember("precision", Node.from(precision))
                .withMember("scale", Node.from(scale))
                .withMember("underlyingType", Node.from(underlyingType.toString()))
                .build();
    }

    public enum UNDERLYING_TYPE {
        FIXED,
        BYTES
    }

    public Integer getPrecision() {
        return precision;
    }

    public Integer getScale() {
        return scale;
    }

    public UNDERLYING_TYPE getUnderlyingType() {
        return underlyingType;
    }



    private AvroDecimalTrait(AvroDecimalTrait.Builder builder) {
        super(ID, builder.getSourceLocation());
        this.precision = Objects.requireNonNull(builder.precision, "precision must be defined");
        this.scale = Objects.requireNonNull(builder.scale, "scale must be defined");
        this.underlyingType = Objects.requireNonNull(builder.underlyingType, "underlyingType must be defined");
      if (precision < 0 || scale < 0) {
            throw new SourceException("precision and scale must be greater than 0", getSourceLocation());
        }
    }

    public static AvroDecimalTrait.Builder builder() {
        return new AvroDecimalTrait.Builder();
    }

    @Override
    public  AvroDecimalTrait.Builder toBuilder() {
        return new AvroDecimalTrait.Builder().sourceLocation(getSourceLocation()).precision(precision).scale(scale).underlyingType(underlyingType);
    }

    public static final class Builder extends AbstractTraitBuilder<AvroDecimalTrait, AvroDecimalTrait.Builder> {
        public UNDERLYING_TYPE underlyingType;
        private Integer precision;
        private Integer scale;

        public AvroDecimalTrait.Builder precision(Integer precision) {
            this.precision = precision;
            return this;
        }

        public AvroDecimalTrait.Builder scale(Integer scale) {
            this.scale = scale;
            return this;
        }

        public AvroDecimalTrait.Builder underlyingType(UNDERLYING_TYPE underlyingType) {
            this.underlyingType = underlyingType;
            return this;
        }

        @Override
        public AvroDecimalTrait build() {
            return new AvroDecimalTrait(this);
        }
    }


    public static final class Provider extends AbstractTrait.Provider {
        public Provider() {
            super(ID);
        }

        @Override
        public Trait createTrait(ShapeId target, Node value) {
            AvroDecimalTrait.Builder builder = builder().sourceLocation(value);

            value.expectObjectNode()
                    .expectNumberMember("scale", n -> builder.scale(n.intValue()))
                    .expectNumberMember("method", n -> builder.precision(n.intValue()))
                    .expectStringMember("underlyingType", (underlyingType) -> {
                        if (underlyingType.equals("fixed")) {
                            builder.underlyingType(UNDERLYING_TYPE.FIXED);
                        } else if (underlyingType.equals("bytes")) {
                            builder.underlyingType(UNDERLYING_TYPE.BYTES);
                        } else {
                            throw new SourceException("underlyingType must be either fixed or bytes", value.getSourceLocation());
                        }
                    });
            return builder.build();
        }
    }
}
