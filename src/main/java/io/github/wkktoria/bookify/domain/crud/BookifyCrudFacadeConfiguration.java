package io.github.wkktoria.bookify.domain.crud;

class BookifyCrudFacadeConfiguration {

    static BookifyCrudFacade createBookifyCrudFacade(final BookRepository bookRepository,
                                                     final GenreRepository genreRepository,
                                                     final AuthorRepository authorRepository,
                                                     final SeriesRepository seriesRepository) {
        BookRetriever bookRetriever = new BookRetriever(bookRepository);
        AuthorRetriever authorRetriever = new AuthorRetriever(authorRepository, bookRetriever);
        GenreDeleter genreDeleter = new GenreDeleter(genreRepository);
        GenreRetriever genreRetriever = new GenreRetriever(genreRepository, bookRetriever);
        GenreAssigner genreAssigner = new GenreAssigner(bookRetriever, genreRetriever);
        BookDeleter bookDeleter = new BookDeleter(bookRepository, bookRetriever, genreDeleter, genreAssigner);
        BookUpdater bookUpdater = new BookUpdater(bookRepository, bookRetriever);
        SeriesAdder seriesAdder = new SeriesAdder(seriesRepository, bookRetriever);
        SeriesRetriever seriesRetriever = new SeriesRetriever(seriesRepository);
        BookAdder bookAdder = new BookAdder(bookRepository, authorRetriever, genreAssigner,
                bookRetriever, seriesRetriever);
        AuthorAdder authorAdder = new AuthorAdder(authorRepository, authorRetriever, bookRetriever, seriesAdder, bookAdder);
        AuthorDeleter authorDeleter = new AuthorDeleter(authorRepository, authorRetriever,
                bookRetriever, bookDeleter);
        AuthorUpdater authorUpdater = new AuthorUpdater(authorRetriever);
        GenreAdder genreAdder = new GenreAdder(genreRepository);
        GenreUpdater genreUpdater = new GenreUpdater(genreRetriever);

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
