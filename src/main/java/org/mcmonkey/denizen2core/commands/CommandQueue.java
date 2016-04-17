package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.ErrorInducedException;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;

import java.util.HashMap;
import java.util.Stack;

/**
 * Represents a set of executing commands.
 */
public class CommandQueue {

    public final Stack<CommandStackEntry> commandStack = new Stack<>();

    public Action<String> error = this::handleError;

    private CommandStackEntry currentEntry = null;

    private CommandEntry waitingOn = null;

    public void waitFor(CommandEntry entry) {
        waitingOn = entry;
    }

    public CommandEntry waitingFor() {
        return waitingOn;
    }

    private double wait = 0;

    public double getWait() {
        return wait;
    }

    public void setWait(double w) {
        wait = w;
    }

    public void start() {
        run(0);
    }

    public boolean shouldShowError() {
        return commandStack.size() == 0 || commandStack.peek().getDebugMode().showMinimal;
    }

    public boolean shouldShowGood() {
        return commandStack.size() == 0 || commandStack.peek().getDebugMode().showFull;
    }

    public void outGood(String message) {
        if (shouldShowGood()) {
            Denizen2Core.getImplementation().outputGood(message);
        }
    }

    public void run(double delta) {
        if (waitingOn != null) {
            return;
        }
        wait -= delta;
        if (wait > 0) {
            return;
        }
        if (wait < 0) {
            wait = 0;
        }
        while (commandStack.size() > 0) {
            currentEntry = commandStack.peek();
            CommandStackEntry.CommandStackRetVal ret = currentEntry.run(this);
            if (ret == CommandStackEntry.CommandStackRetVal.BREAK) {
                return;
            }
            else if (ret == CommandStackEntry.CommandStackRetVal.STOP) {
                break;
            }
        }
    }

    public void stop() {
        commandStack.clear();
        throw new ErrorInducedException("Stopping queue...");
    }

    public void handleError(String error) {
        if (shouldShowError()) {
            Debug.error(error);
        }
        stop();
    }

    public void handleError(CommandEntry entry, String error) {
        if (shouldShowError()) {
            Debug.error(error);
        }
        stop();
    }
}
