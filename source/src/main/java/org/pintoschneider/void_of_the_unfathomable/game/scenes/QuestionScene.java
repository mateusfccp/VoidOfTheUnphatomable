package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Border;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.CrossAxisAlignment;
import org.pintoschneider.void_of_the_unfathomable.ui.core.EdgeInsets;

import java.util.List;

/**
 * A scene that presents a question with multiple selectable answers.
 *
 * @param <T> The type of the answer options, which must be an enum.
 */
public final class QuestionScene<T> extends SelectionScene {
    private final String question;
    private final List<Answer<T>> answers;

    /**
     * Creates a new question scene with the specified question and answers.
     *
     * @param question The question to be displayed.
     * @param answers  The list of possible answers.
     */
    public QuestionScene(String question, List<Answer<T>> answers) {
        this.question = question;
        this.answers = answers;
    }

    /**
     * Creates a yes/no question scene.
     *
     * @param question The question to be displayed.
     * @return A {@link QuestionScene} with "Yes" and "No" answers.
     */
    public static QuestionScene<Boolean> yesNo(String question) {
        return new QuestionScene<>(
            question,
            List.of(
                new Answer<>("Sí", true, true),
                new Answer<>("No", false, true)
            )
        );
    }

    @Override
    List<Option> options() {
        return answers.stream()
            .map(Answer::toOption)
            .toList();
    }

    @Override
    public Component build() {
        return new Padding(
            EdgeInsets.symmetric(2, 4),
            new Column(
                new Box(
                    Border.SINGLE_ROUNDED,
                    new Padding(
                        EdgeInsets.all(1),
                        new Text(question)
                    )
                ),
                new SizedBox(0, 1),
                super.build()
            ).crossAxisAlignment(CrossAxisAlignment.END)
        );
    }

    /**
     * Represents an answer in the questin scene.
     *
     * @param label   The label of the option.
     * @param value   The value of the option.
     * @param enabled Whether the option is enabled or disabled.
     * @param <T>     The type of the answer value.
     */
    public record Answer<T>(String label, T value, boolean enabled) {
        private Option toOption() {
            if (enabled) {
                return new Option(label, () -> Engine.context().sceneManager().pop(value));
            } else {
                return new Option(label);
            }
        }
    }
}
