<?php
include '../config/config.php';

// Vérifiez que la méthode est POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Récupération des données JSON
    $input = json_decode(file_get_contents('php://input'), true);
    
    // Vérifiez si intervention_id est présent
    if (isset($input['intervention_id'])) {
        $intervention_id = $input['intervention_id'];
        
        // Préparation de la requête
        $query = $conn->prepare("SELECT sites.longitude, sites.latitude, sites.adresse 
                                 FROM sites 
                                 JOIN interventions ON interventions.site_id = sites.id 
                                 WHERE interventions.id = :intervention_id");
        $query->execute(['intervention_id' => $intervention_id]);
        
        $location = $query->fetch(PDO::FETCH_ASSOC);
        
        if ($location) {
            echo json_encode($location);
        } else {
            echo json_encode(["error" => "Location not found for this intervention."]);
        }
    } else {
        echo json_encode(["error" => "Invalid request. 'intervention_id' is required."]);
    }
} else {
    echo json_encode(["error" => "Invalid request method. Use POST."]);
}
?>
