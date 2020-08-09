package ir.sam.hearthstone.server.controller.logic.game.api;

import ir.sam.hearthstone.server.resource_loader.ModelLoader;
import ir.sam.hearthstone.server.util.hibernate.Connector;

public interface OnlineGameBuilderGenerator {
    OnlineGameBuilder generate(ModelLoader modelLoader, Connector connector);
}
