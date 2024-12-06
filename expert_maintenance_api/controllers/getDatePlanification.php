<?php
include '../config/config.php';

if (isset($_GET['id'])) {
    $interventionId = intval($_GET['id']);
    
    // Préparer la requête pour récupérer la date de planification
    $query = $conn->prepare("
        SELECT 
            dateplanification 
        FROM 
            interventions 
        WHERE 
            id = :id
    ");
    
    $query->bindParam(':id', $interventionId, PDO::PARAM_INT);
    $query->execute();

    // Vérifier si une intervention a été trouvée
    if ($query->rowCount() > 0) {
        $result = $query->fetch(PDO::FETCH_ASSOC);

        // Retourner la date de planification en JSON
        echo json_encode([
            'data' => [
                'dateplanification' => $result['dateplanification']
            ]
        ]);
    } else {
        echo json_encode([
          
            'message' => 'Intervention non trouvée'
        ]);
    }
} else {
    echo json_encode([
      
        'message' => 'Paramètre ID manquant'
    ]);
}
?>
