package io.github.wkktoria.bookify.domain.crud;

class BookifyCrudFacadeConfiguration {

    static BookifyCrudFacade createBookifyCrudFacade(final BookRepository bookRepository,
                                                     final GenreRepository genreRepository,
                                                     final AuthorRepository authorRepository,
                                                     final SeriesRepository seriesRepository) {
        BookRetriever bookRetriever = new BookRetriever(bookRepository);
        AuthorRetriever authorRetriever = new AuthorRetriever(authorRepository, bookRetriever);
        GenreRetriever genreRetriever = new GenreRetriever(genreRepository, bookRetriever);
        GenreDeleter genreDeleter = new GenreDeleter(genreRetriever, genreRepository);
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
        SeriesDeleter seriesDeleter = new SeriesDeleter(seriesRetriever, seriesRepository);

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
                genreDeleter,
                seriesAdder,
                seriesRetriever,
                seriesDeleter
        );
    }

}
