<?php
include '../config/config.php';

header('Content-Type: application/json');

// Check for required parameters
if (!isset($_GET['employee_id']) || !isset($_GET['date'])) {
    http_response_code(400); // Bad Request
    echo json_encode(["error" => "Missing parameters."]);
    exit;
}

$employee_id = $_GET['employee_id'];
$date = $_GET['date'];

// Validate date format (YYYY-MM-DD)
$dateTime = DateTime::createFromFormat('Y-m-d', $date);
if (!$dateTime || $dateTime->format('Y-m-d') !== $date) {
    http_response_code(400); // Bad Request
    echo json_encode(["error" => "Invalid date format. Expected format is YYYY-MM-DD."]);
    exit;
}

try {
    // Prepare SQL query
    $query = "
        SELECT i.id, i.titre, i.datedebut, i.datefin, i.heuredebutplan, i.heurefinplan,
               c.nom AS client_name, s.adresse AS site_address, s.ville AS site_city
        FROM interventions i
        JOIN employes_interventions ei ON i.id = ei.intervention_id
        JOIN sites s ON s.id = i.site_id
        JOIN clients c ON c.id = s.client_id
        WHERE ei.employe_id = :employee_id
          AND (:date BETWEEN i.datedebut AND i.datefin)
    ";

    // Execute prepared statement
    $stmt = $conn->prepare($query);
    $stmt->bindParam(':employee_id', $employee_id, PDO::PARAM_INT);
    $stmt->bindParam(':date', $date, PDO::PARAM_STR);
    $stmt->execute();

    $interventions = $stmt->fetchAll(PDO::FETCH_ASSOC);

    // Check if interventions are found
    if ($interventions) {
        echo json_encode($interventions);
    } else {
        http_response_code(404); // Not Found
        echo json_encode(["message" => "No interventions found for the specified date."]);
    }
} catch (PDOException $e) {
    http_response_code(500); // Internal Server Error
    // Log error to a file instead of showing it to the user in production
    error_log("Database error: " . $e->getMessage(), 3, "/var/log/app_errors.log");
    echo json_encode(["error" => "A server error occurred. Please try again later."]);
}
?>
