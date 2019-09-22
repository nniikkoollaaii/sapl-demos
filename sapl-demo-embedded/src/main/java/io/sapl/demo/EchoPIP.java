package io.sapl.demo;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import io.sapl.api.pip.Attribute;
import io.sapl.api.pip.PolicyInformationPoint;
import io.sapl.api.validation.Text;

@PolicyInformationPoint(name = "echo", description = "Echo PIP")
public class EchoPIP {

    @Attribute(name = "echo")
    public JsonNode echo(@Text JsonNode value,
                                       Map<String, JsonNode> variables) {
        return value;
    }
}
