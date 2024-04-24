package example;

import example.person.Person;
import example.person.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class ExampleControllerTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private ExampleController exampleController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        exampleController = new ExampleController(personRepository);
    }

    @Test
    @DisplayName("Возврашение фразы 'Здравствуй Мир!'")
    public void shouldReturnHelloWorld() {
        assertEquals("Здравствуй Мир!", exampleController.hello());
    }

    @Test
    @DisplayName("Возвращение полного имени человека")
    public void shouldReturnFullNameOfAPerson() throws Exception {
        Person peter = new Person("Иван", "Иванов");
        given(personRepository.findByLastName("Иванов")).willReturn(Optional.of(peter));

        var greeting = exampleController.hello("Иванов");

        assertEquals("Здравствуй, Иван Иванов!", greeting);
    }

    @Test
    @DisplayName("Сообщение, что человек неизвестен")
    public void shouldTellIfPersonIsUnknown() throws Exception {
        given(personRepository.findByLastName(anyString())).willReturn(Optional.empty());

        var greeting = exampleController.hello("Иванов");

        assertEquals("Не знаю никого по фамилии 'Иванов'", greeting);
    }
}