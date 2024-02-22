$version: "2"

namespace coalmine


@protocolDefinition(traits: [
    avroRequest,
    avroOneWay
])
@trait(selector: "service")
structure rpcAvro {
}

@trait(selector: "operation")
string avroRequest

@trait(selector: "operation")
string avroOneWay
