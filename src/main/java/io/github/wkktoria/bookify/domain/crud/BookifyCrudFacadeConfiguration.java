package io.github.wkktoria.bookify.domain.crud;

class BookifyCrudFacadeConfiguration {

    static BookifyCrudFacade createBookifyCrudFacade(final BookRepository bookRepository,
                                                     final GenreRepository genreRepository,
                                                     final AuthorRepository authorRepository,
                                                     final SeriesRepository seriesRepository) {
        BookRetriever bookRetriever = new BookRetriever(bookRepository);
        AuthorRetriever authorRetriever = new AuthorRetriever(authorRepository, bookRetriever);
        BookDeleter bookDeleter = new BookDeleter(bookRepository, bookRetriever);
        BookUpdater bookUpdater = new BookUpdater(bookRepository, bookRetriever);
        AuthorAdder authorAdder = new AuthorAdder(authorRepository, authorRetriever, bookRetriever);
        AuthorDeleter authorDeleter = new AuthorDeleter(authorRepository, authorRetriever,
                bookRetriever, bookDeleter);
        AuthorUpdater authorUpdater = new AuthorUpdater(authorRetriever);
        GenreRetriever genreRetriever = new GenreRetriever(genreRepository, bookRetriever);
        GenreAdder genreAdder = new GenreAdder(genreRepository);
        GenreUpdater genreUpdater = new GenreUpdater(genreRetriever);
        GenreAssigner genreAssigner = new GenreAssigner(bookRetriever, genreRetriever);
        SeriesRetriever seriesRetriever = new SeriesRetriever(seriesRepository);
        BookAdder bookAdder = new BookAdder(bookRepository, authorRetriever, genreAssigner,
                bookRetriever, seriesRetriever);
        SeriesAdder seriesAdder = new SeriesAdder(seriesRepository, bookRetriever);
        return new BookifyCrudFacade(
                bookAdder,
                bookRetriever,
                bookDeleter,
                bookUpdater,
                authorAdder,
                authorRetriever,
                authorDeleter,
                authorUpdater,
                genreRetriever,
                genreAdder,
                genreUpdater,
                genreAssigner,
                seriesAdder,
                seriesRetriever
        );
    }

}
