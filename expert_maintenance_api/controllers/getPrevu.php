<?php
include '../config/config.php';

if (isset($_GET['id'])) {
    $interventionId = intval($_GET['id']);
    
    // Préparer la requête pour récupérer les données
    $query = $conn->prepare("
        SELECT 
            heuredebutplan, 
            heurefinplan, 
            commentaires, 
            TIMEDIFF(heurefinplan, heuredebutplan) AS duree 
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

        // Retourner les données en JSON
        echo json_encode([
            'data' => [
                'heuredebutplan' => $result['heuredebutplan'],
                'heurefinplan' => $result['heurefinplan'],
                'commentaires' => $result['commentaires'],
                'duree' => $result['duree']
            ]
        ]);
    } else {
        echo json_encode([
            'success' => false,
            'message' => 'Intervention non trouvée'
        ]);
    }
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Paramètre ID manquant'
    ]);
}
?>
