<?php
include '../config/config.php';

if (isset($_GET['id'])) {
    $interventionId = intval($_GET['id']);

    try {
        // Démarrer une transaction
        $conn->beginTransaction();

        // Supprimer les données liées à l'intervention
        $tablesToDelete = [
            'employes_interventions', // Table des employés assignés
            'images',                // Table des images
            'taches'                 // Table des tâches
        ];

        foreach ($tablesToDelete as $table) {
            $query = $conn->prepare("DELETE FROM $table WHERE intervention_id = :id");
            $query->bindParam(':id', $interventionId, PDO::PARAM_INT);
            $query->execute();
        }

        // Supprimer l'intervention elle-même
        $query = $conn->prepare("DELETE FROM interventions WHERE id = :id");
        $query->bindParam(':id', $interventionId, PDO::PARAM_INT);
        $query->execute();

        // Valider la transaction
        $conn->commit();

        // Retourner un succès
        echo json_encode([
           
            'message' => 'Intervention et ses données liées supprimées avec succès.'
        ]);
    } catch (Exception $e) {
        // Annuler la transaction en cas d'erreur
        $conn->rollBack();

        // Retourner une erreur
        echo json_encode([
            
            'message' => 'Une erreur est survenue lors de la suppression : ' . $e->getMessage()
        ]);
    }
} else {
    echo json_encode([
        
        'message' => 'Paramètre ID manquant'
    ]);
}
?>
