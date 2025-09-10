package com.ping.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalOperationToolTest {

    @Test
    void executeTerminalCommand() {
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        String command = "ls -l";
        String result = terminalOperationTool.executeTerminalCommand(command);
        Assertions.assertNotNull(result);
    }
}