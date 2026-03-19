package io.github.wkktoria.bookify.domain.crud;

class BookifyCrudFasadeConfiguration {

    static BookifyCrudFacade createBookifyCrudFasade(final BookRepository bookRepository,
                                                     final GenreRepository genreRepository,
                                                     final AuthorRepository authorRepository,
                                                     final SeriesRepository seriesRepository) {
        AuthorRetriever authorRetriever = new AuthorRetriever(authorRepository);
        BookAdder bookAdder = new BookAdder(bookRepository, authorRetriever);
        BookRetriever bookRetriever = new BookRetriever(bookRepository);
        BookDeleter bookDeleter = new BookDeleter(bookRepository, bookRetriever);
        BookUpdater bookUpdater = new BookUpdater(bookRepository, bookRetriever);
        AuthorAdder authorAdder = new AuthorAdder(authorRepository);
        GenreAdder genreAdder = new GenreAdder(genreRepository);
        SeriesAdder seriesAdder = new SeriesAdder(seriesRepository, bookRetriever);
        SeriesRetriever seriesRetriever = new SeriesRetriever(seriesRepository);
        return new BookifyCrudFacade(
                bookAdder,
                bookRetriever,
                bookDeleter,
                bookUpdater,
                authorAdder,
                authorRetriever,
                genreAdder,
                seriesAdder,
                seriesRetriever
        );
    }
    
}
