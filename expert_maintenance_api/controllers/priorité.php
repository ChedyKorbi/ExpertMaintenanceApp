<?php
include '../config/config.php';

if (isset($_GET['id'])) {
    $interventionId = intval($_GET['id']);
    
    // Préparer la requête pour récupérer le nom de la priorité
    $query = $conn->prepare("
        SELECT 
            priorites.nom AS priorite
        FROM 
            interventions
        INNER JOIN 
            priorites ON interventions.priorite_id = priorites.id
        WHERE 
            interventions.id = :id
    ");
    
    $query->bindParam(':id', $interventionId, PDO::PARAM_INT);
    $query->execute();

    // Vérifier si une intervention a été trouvée
    if ($query->rowCount() > 0) {
        $result = $query->fetch(PDO::FETCH_ASSOC);

        // Retourner le nom de la priorité en JSON
        echo json_encode([
            'data' => [
                'priorite' => $result['priorite']
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
