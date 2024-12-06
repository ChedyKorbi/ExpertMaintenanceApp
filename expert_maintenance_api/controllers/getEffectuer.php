<?php
include '../config/config.php';

if (isset($_GET['id'])) {
    $interventionId = intval($_GET['id']);
    
    // Prepare the query
    $query = $conn->prepare("
        SELECT 
            terminee,
            DATE_FORMAT(datedebut, '%d/%m/%Y') AS formatted_datedebut,
            DATE_FORMAT(datefin, '%d/%m/%Y') AS formatted_datefin,
            DATE_FORMAT(heuredebutef, '%H:%i:%s') AS formatted_heuredebutplan,
            DATE_FORMAT(heurefinef, '%H:%i:%s') AS formatted_heurefinplan,
            TIMEDIFF(heurefinef, heuredebutef) AS estimated_duration_time,
            commentaires
        FROM 
            interventions 
        WHERE 
            id = :id
    ");

    $query->bindParam(':id', $interventionId, PDO::PARAM_INT);
    $query->execute();

    // Check if an intervention is found
    if ($query->rowCount() > 0) {
        $result = $query->fetch(PDO::FETCH_ASSOC);

        // Calculate estimated duration in hours
        $durationParts = explode(':', $result['estimated_duration_time']);
        $durationHours = $durationParts[0] + ($durationParts[1] / 60);

        // Build JSON response
        echo json_encode([
            'data' => [
                'terminee' => $result['terminee'] == 1 ? 'Oui' : 'Non',
                'datedebutplan' => $result['formatted_datedebut'],
                'datefinplan' => $result['formatted_datefin'],
                'heuredebutplan' => $result['formatted_heuredebutplan'],
                'heurefinplan' => $result['formatted_heurefinplan'],
                'temps_estime' => round($durationHours, 2) . ' heures',
                'commentaires' => $result['commentaires']
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
