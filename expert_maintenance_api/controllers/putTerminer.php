<?php
include '../config/config.php';

// Vérifier si les paramètres 'id' et 'terminee' sont présents dans l'URL
if (isset($_GET['id']) && isset($_GET['terminee'])) {
    // Récupérer les valeurs des paramètres dans l'URL
    $interventionId = intval($_GET['id']);  // ID de l'intervention
    $terminee = intval($_GET['terminee']);  // 0 ou 1 pour "terminée" ou "non terminée"

    // Préparer la requête SQL pour mettre à jour la colonne `terminee`
    $query = $conn->prepare("
        UPDATE interventions
        SET terminee = :terminee, dateterminaison = NOW()
        WHERE id = :interventionId
    ");

    $query->bindParam(':terminee', $terminee, PDO::PARAM_INT);
    $query->bindParam(':interventionId', $interventionId, PDO::PARAM_INT);

    try {
        $query->execute();

        if ($query->rowCount() > 0) {
            echo json_encode([
                'message' => 'Intervention mise à jour avec succès'
            ]);
        } else {
            echo json_encode([
                'message' => 'Aucune intervention mise à jour'
            ]);
        }
    } catch (Exception $e) {
        echo json_encode([
            'message' => 'Erreur lors de la mise à jour : ' . $e->getMessage()
        ]);
    }
} else {
    echo json_encode([
        'message' => 'Paramètres manquants'
    ]);
}
?>
