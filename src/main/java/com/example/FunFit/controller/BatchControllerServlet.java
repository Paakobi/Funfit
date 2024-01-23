package com.example.FunFit.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.FunFit.database.Database;
import com.example.FunFit.model.Batch;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class BatchControllerServlet
 */
public class BatchControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BatchControllerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// Create a Batch object and set its properties

		Batch batch = new Batch();
		batch.setBid(Integer.parseInt(request.getParameter("bid")));
		batch.setBatchName(request.getParameter("batchName"));
		batch.setBatchInstructor(request.getParameter("batchInstructor"));
		batch.setCapacity(50);
		batch.setBatchDuration(Integer.parseInt(request.getParameter("batchDuration")));

		// Use the database singleton instance
		Database db = Database.getInstance();

		boolean combinationDoesExist = false;

		String checkIFBatchName_ID_unique = "SELECT COUNT(*) AS count FROM Batch WHERE bid = ? AND batchName = ?";

		// checking to see what the batchID associated with the given Batch name
		try (Connection connection = db.getConnection();
				PreparedStatement ps = connection.prepareStatement(checkIFBatchName_ID_unique)) {

			// Set the batch name parameter
			ps.setInt(1, batch.getBid());
			ps.setString(2, batch.getBatchName());

			try (ResultSet resultSet = db.executeQuery(ps)) {
				if (resultSet != null && resultSet.next()) {
					int count = resultSet.getInt("count");
					combinationDoesExist = count > 0;
					// Set the batch ID in the participant object
					//System.out.println("sanity check: " + resultSet.getInt("bid"));
					//batch.setBid(resultSet.getInt("bid"));
					
				
				}
				
			}
			
				
				if(!combinationDoesExist) {
					
					// SQL query to insert batch data into the database

					String inserBatchSql = "INSERT INTO Batch (batchName, batchInstructor, capacity, batchDuration, bid) VALUES (?, ?, ?, ?, ?)";
					
					try (PreparedStatement ps2 = connection.prepareStatement(inserBatchSql)) {

						// Set parameters for the batch insertion
						ps2.setString(1, batch.getBatchName());
						ps2.setString(2, batch.getBatchInstructor());
						ps2.setInt(3, batch.getCapacity());
						ps2.setInt(4, batch.getBatchDuration());
						// System.out.println("sanityChk2" + batch.getBid());
						ps2.setInt(5, batch.getBid());
						
						// Execute the update
						int result = db.executeUpdate(ps2);

						// Begin sending results to Front END using JSP.
						if (result > 0) {
							// Set attributes for data that the JSP will use to generate the view
							request.setAttribute("successMessage", "Batch added successfully!");
							request.setAttribute("batchName", batch.getBatchName());
							request.setAttribute("batchInstructor", batch.getBatchInstructor());
							request.setAttribute("batchCapacity", batch.getCapacity());
							request.setAttribute("batchDuration", batch.getBatchDuration());

							// Forward the request to the JSP for rendering the view
							RequestDispatcher dispatcher = request.getRequestDispatcher("/add-batch.jsp");
							dispatcher.forward(request, response);
					
				}
				else {
					// Handle the case where no rows were found
				}
			}catch (Exception e) {
						e.printStackTrace();
						
					} 
					
				}else {
						// Handle the case where the combination already exists
						System.out.println("Batch with the same batchid and batchName already exists.");
					}
		
				// Implement the batchExists and insertBatch methods accordingly
		} catch (SQLException e) {
			e.printStackTrace();
		}
	/*	// SQL query to insert batch data into the database

		//String inserBatchSql = "INSERT INTO Batch (batchName, batchInstructor, capacity, batchDuration, bid) VALUES (?, ?, ?, ?, ?)";
		
		//try (Connection connection = db.getConnection();
			//	PreparedStatement ps2 = connection.prepareStatement(inserBatchSql)) {

			// Set parameters for the batch insertion
			//ps2.setString(1, batch.getBatchName());
			//ps2.setString(2, batch.getBatchInstructor());
			//ps2.setInt(3, batch.getCapacity());
			//ps2.setInt(4, batch.getBatchDuration());
			// System.out.println("sanityChk2" + batch.getBid());
			//ps2.setInt(5, batch.getBid());
			
			// Execute the update
			int result = db.executeUpdate(ps2);

			// Begin sending results to Front END using JSP.
			if (result > 0) {
				// Set attributes for data that the JSP will use to generate the view
				request.setAttribute("successMessage", "Batch added successfully!");
				request.setAttribute("batchName", batch.getBatchName());
				request.setAttribute("batchInstructor", batch.getBatchInstructor());
				request.setAttribute("batchCapacity", batch.getCapacity());
				request.setAttribute("batchDuration", batch.getBatchDuration());

				// Forward the request to the JSP for rendering the view
				RequestDispatcher dispatcher = request.getRequestDispatcher("/add-batch.jsp");
				dispatcher.forward(request, response);
			} else {
				// Handle the case where no rows were found
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
	}

}
