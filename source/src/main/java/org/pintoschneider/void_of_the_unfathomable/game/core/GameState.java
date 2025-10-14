package org.pintoschneider.void_of_the_unfathomable.game.core;

public sealed class GameState {
}

final class MainMenuState extends GameState {}

final class InGameState extends GameState {}

final class PausedState extends GameState {}