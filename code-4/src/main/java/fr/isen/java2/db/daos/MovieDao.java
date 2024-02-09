package fr.isen.java2.db.daos;

import static fr.isen.java2.db.daos.DataSourceFactory.getDataSource;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;
import fr.isen.java2.db.entities.Movie;

public class MovieDao {
	
	
    
	public List<Movie> listMovies() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM movie JOIN genre ON movie.genre_id = genre.idgenre";
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getInt("idmovie"));
                movie.setTitle(resultSet.getString("title"));
                movie.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
                movie.setGenre(new Genre (resultSet.getInt("genre_id"),resultSet.getString("name")).getId());
                movie.setDuration(resultSet.getInt("duration"));
                movie.setDirector(resultSet.getString("director"));
                movie.setSummary(resultSet.getString("summary"));
                
                
                
                
                movies.add(movie);
            }
            return movies;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des genres", e);
        }
        
    }
		    
	

	public List<Movie> listMoviesByGenre() {
	    String query = "SELECT * FROM movie JOIN genre ON movie.genre_id = genre.idgenre WHERE genre.name = 'Comedy'";
	    List<Movie> moviesByGenre = new ArrayList<>();
	    try (Connection connection = getDataSource().getConnection();
	        PreparedStatement statement = connection.prepareStatement(query)) {
	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {
	                Movie movie = new Movie();
	                movie.setId(resultSet.getInt("idmovie"));
	                movie.setTitle(resultSet.getString("title"));
	                movie.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
	                movie.setGenre(new Genre (resultSet.getInt("genre_id"),resultSet.getString("name")).getId());
	                movie.setDuration(resultSet.getInt("duration"));
	                movie.setDirector(resultSet.getString("director"));
	                movie.setSummary(resultSet.getString("summary"));
	                    
	                moviesByGenre.add(movie);
	              }
	         }
	    } catch (SQLException e) {
	           throw new RuntimeException("Erreur lors de la récupération des films par genre", e);
	        }
	      return moviesByGenre;
	    }

	public void addMovie(String title) {
		String query = "INSERT INTO movie(title,release_date,genre_id,duration,director,summary) VALUES(?,?,?,?,?,?)";
		try (Connection connection = getDataSource().getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(query)) {
            	statement.setString(1, title);
                statement.setObject(2, LocalDate.of(2015,11,14)); // Date de sortie actuelle
                statement.setInt(3, 2); // le genre "Comedy" a l'ID 2 dans la base de données
                statement.setInt(4, 120); // la durée de film de 120 minutes
                statement.setString(5, "Director 5"); // réalisateur 
                statement.setString(6, "Summary of the movie"); // Résumé du film
                statement.executeUpdate();
            }
		
        
		} 	catch (SQLException e) {
        	throw new RuntimeException("Erreur lors de l'ajout du genre", e);
    	}
	}
}
