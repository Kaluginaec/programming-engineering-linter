import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.FileReader;
import ru.yarsu.Calculator;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.within;

public class CalculatorTest {
    @Nested
    class InfixToPostfixTest {
        @Test
        void simple() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("a", "+", "b", "*", "c", "-", "d")))).containsExactly("a", "b", "c", "*", "+", "d", "-");
        }

        @Test
        void additionSubtraction() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("5", "+", "2", "-", "3")))).containsExactly("5", "2", "+", "3", "-");
        }

        @Test
        void multiplicationDivision() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("5", "*", "2", "/", "3")))).containsExactly("5", "2", "*", "3", "/");
        }

        @Test
        void power() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("k", "*", "2", "^", "3")))).containsExactly("k", "2", "3", "^", "*");
        }

        @Test
        void power2() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("3", "+", "k", "*", "2", "^", "3")))).containsExactly("3", "k", "2", "3", "^", "*", "+");
        }

        // ассоциативность возведения в степень: операцию выполняем справа налево
        @Test
        void twoPowers() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("k", "^", "2", "^", "3", "+", "5")))).containsExactly("k", "2", "3", "^", "^", "5", "+");
        }


        @Test
        void brackets() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("(", "a", "+", "b", ")", "*", "c", "-", "d")))).containsExactly("a", "b", "+", "c", "*", "d", "-");
        }

        @Test
        void twoBrackets() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("(", "a", "+", "b", ")", "*", "(", "c", "-", "d", ")")))).containsExactly("a", "b", "+", "c", "d", "-", "*");
        }

        @Test
        void twoOperationsInBrackets() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("k", "*", "(", "a", "*", "b", "+", "c", ")", "-", "3")))).containsExactly("k", "a", "b", "*", "c", "+", "*", "3", "-");
        }

        @Test
        void twoOperationsInBrackets2() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("k", "+", "(", "a", "+", "b", "*", "c", ")", "/", "3")))).containsExactly("k", "a", "b", "c", "*", "+", "3", "/", "+");
        }


        @Test
        void nestedBrackets() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("(", "(", "2", "-", "3", ")", ")")))).containsExactly("2", "3", "-");
        }

        @Test
        void singleElement() {
            assertThat(Calculator.infixToPostfix(new ArrayList<>(List.of("6")))).containsExactly("6");
        }

        @Test
        void missOpenBracketThrowsException() {
            assertThatThrownBy(() -> Calculator.infixToPostfix(new ArrayList<>(List.of("2", "+", "3", ")", "*", "5")))).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void missCloseBracketThrowsException() {
            assertThatThrownBy(() -> Calculator.infixToPostfix(new ArrayList<>(List.of("(", "2", "+", "3", "*", "5")))).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class ResultByInfix {
        @Test
        void simple() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("3", "+", "4", "*", "2", "-", "7"))))).isEqualTo(4);
        }

        @Test
        void additionSubtraction() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("5", "+", "2", "-", "3"))))).isEqualTo(4);
        }

        @Test
        void multiplicationDivision() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("5", "*", "2", "/", "3"))))).isCloseTo(3.333, within(0.001));
        }

        @Test
        void power() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("4", "*", "2", "^", "3"))))).isEqualTo(32);
        }

        @Test
        void power2() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("3", "+", "6", "*", "2", "^", "3"))))).isEqualTo(51);
        }

        // ассоциативность возведения в степень: операцию выполняем справа налево
        @Test
        void twoPowers() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("2", "^", "2", "^", "3", "+", "5"))))).isEqualTo(261);
        }


        @Test
        void brackets() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("(", "3", "+", "4", ")", "*", "5", "-", "3"))))).isEqualTo(32);
        }

        @Test
        void twoBrackets() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("(", "3", "+", "4", ")", "*", "(", "5", "-", "3", ")"))))).isEqualTo(14);
        }

        @Test
        void twoOperationsInBrackets() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("2", "*", "(", "3", "*", "4", "+", "5", ")", "-", "3"))))).isEqualTo(31);
        }

        @Test
        void twoOperationsInBrackets2() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("2", "+", "(", "3", "+", "4", "*", "5", ")", "/", "3"))))).isCloseTo(9.666, within(0.001));
        }


        @Test
        void nestedBrackets() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("(", "(", "2", "-", "3", ")", ")"))))).isEqualTo(-1);
        }

        @Test
        void singleElement() {
            assertThat(Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("6"))))).isEqualTo(6);
        }

        @Test
        void missOpenBracketThrowsException() {
            assertThatThrownBy(() -> Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("2", "+", "3", ")", "*", "5"))))).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void missCloseBracketThrowsException() {
            assertThatThrownBy(() -> Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("(", "2", "+", "3", "*", "5"))))).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void notNumbersAndOperatorsThrowsException() {
            assertThatThrownBy(() -> Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("(", "2", "+", "b", ")", "*", "5"))))).isInstanceOf(NumberFormatException.class);
        }

        @Test
        void UnknownOperatorThrowsException() {
            assertThatThrownBy(() -> Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("5", "&", "3"))))).isInstanceOf(NumberFormatException.class);
        }

        @Test
        void uncorrectedStackThrowsException() {
            assertThatThrownBy(() -> Calculator.resultByPostfix(new ArrayList<>(List.of("2", "+")))).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void uncorrectedStack2ThrowsException() {
            assertThatThrownBy(() -> Calculator.resultByPostfix(new ArrayList<>(List.of("2", "3")))).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void divisionByZero() {
            assertThatThrownBy(() -> Calculator.resultByPostfix(Calculator.infixToPostfix(new ArrayList<>(List.of("5", "/", "0"))))).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
