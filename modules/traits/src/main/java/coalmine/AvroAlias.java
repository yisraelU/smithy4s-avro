package coalmine;

import software.amazon.smithy.model.SourceLocation;
import software.amazon.smithy.model.node.ArrayNode;
import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.node.StringNode;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AbstractTrait;
import software.amazon.smithy.model.traits.Trait;

import java.util.Set;
import java.util.stream.Collectors;

public class AvroAlias extends AbstractTrait {
    public static final ShapeId ID = ShapeId.from("coalmine#avroAliases");

    private final Set<String> aliases;

    public Set<String> getAliases() {
        return aliases;
    }


    public AvroAlias(Set<String> aliases, SourceLocation sourceLocation) {
        super(ID, sourceLocation);
        this.aliases = aliases;
    }

    public AvroAlias(Set<String> aliases) {
        this(aliases, SourceLocation.NONE);
    }

    @Override
    protected Node createNode() {
        return null;
    }

    public static final class Provider extends AbstractTrait.Provider {
        public Provider() {
            super(ID);
        }

        @Override
        public Trait createTrait(ShapeId target, Node value) {
            ArrayNode array = value.expectArrayNode();
            Set<String> aliases = array.getElementsAs(StringNode.class).stream()
                    .map(StringNode::getValue)
                    .collect(Collectors.toSet());
            AvroAlias trait = new AvroAlias(aliases, value.getSourceLocation());
            trait.setNodeCache(value);
            return trait;
        }

    }
}