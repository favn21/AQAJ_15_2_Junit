package exercise.article;

import exercise.worker.WorkerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkerImplTest {
    @Mock
    private Library library;

    @Mock
    private Article article;

    @InjectMocks
    private WorkerImpl worker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Подготовка статей")
    public void testPrepareArticles() {
        Article article1 = new Article("Title 1", "Content 1", "Author 1", null);
        Article article2 = new Article("Title 2", "Content 2", null, LocalDate.parse("2024-04-20"));
        Article article3 = new Article("Title 3", "Content 3", "Author 3", LocalDate.parse("2024-04-21"));
        List<Article> articles = Arrays.asList(article1, article2, article3);

        List<Article> preparedArticles = worker.prepareArticles(articles);

        assertNotNull(preparedArticles);

        preparedArticles.forEach(article -> {
            assertNotNull(article.getCreationDate());
        });
    }

    @Test
    @DisplayName("Добавление новых статей")
    public void testAddNewArticles() {
        Article article1 = new Article("Title 1", "Content 1", "Author 1", LocalDate.parse("2023-01-01"));
        Article article2 = new Article("Title 2", "Content 2", "Author 2", LocalDate.parse("2024-01-01"));
        Article article3 = new Article("Title 3", "Content 3", "Author 3", LocalDate.parse("2024-01-01"));
        List<Article> articles = Arrays.asList(article1, article2, article3);

        when(library.getAllTitles()).thenReturn(Collections.singletonList("Title 1"));

        doNothing().when(library).store(anyInt(), anyList());

        worker.addNewArticles(articles);

        verify(library, times(2)).store(anyInt(), anyList());
        verify(library, times(1)).updateCatalog();
    }

    @Test
    @DisplayName("Получение каталога")
    public void testGetCatalog() {
        List<String> titles = Arrays.asList("Title 3", "Title 1", "Title 2");

        when(library.getAllTitles()).thenReturn(titles);

        String expectedCatalog = "Список доступных статей:\n" +
                "    Title 1\n" +
                "    Title 2\n" +
                "    Title 3\n";

        String actualCatalog = worker.getCatalog();

        assertEquals(expectedCatalog, actualCatalog);
    }

    @Test
    @DisplayName("Подготовка статей с дублирующимися заголовками")
    public void testPrepareArticlesWithDuplicateTitles() {
        Article article1 = new Article("Title 1", "Content 1", "Author 1", LocalDate.parse("2023-01-01"));
        Article article2 = new Article("Title 2", "Content 2", "Author 2", LocalDate.parse("2024-01-01"));
        Article article3 = new Article("Title 1", "Content 3", "Author 3", LocalDate.parse("2024-01-01"));
        List<Article> articles = Arrays.asList(article1, article2, article3);

        List<Article> preparedArticles = worker.prepareArticles(articles);

        assertEquals(2, preparedArticles.size());
    }
}


