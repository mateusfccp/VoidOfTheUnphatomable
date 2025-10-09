package org.pintoschneider.void_of_the_unphatomable.game;

public sealed class GameState {
}

final class MainMenuState extends GameState {}

final class InGameState extends GameState {}

final class PausedState extends GameState {}