<?php
include '../config/config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Get the user ID to delete data associated with this user
    $userId = isset($_POST['user_id']) ? $_POST['user_id'] : null;

    if ($userId === null) {
        echo json_encode(["success" => false, "message" => "User ID is required."]);
        exit;
    }

    try {
        $conn->beginTransaction();

        // Deleting from related tables first to avoid foreign key constraint violations
        // Delete from `employes_interventions` for interventions linked to this user
        $query = $conn->prepare("DELETE ei FROM employes_interventions ei
                                 INNER JOIN interventions i ON ei.intervention_id = i.id
                                 WHERE i.user_id = :user_id");
        $query->execute(['user_id' => $userId]);

        // Delete from `images` table linked to interventions
        $query = $conn->prepare("DELETE img FROM images img
                                 INNER JOIN interventions i ON img.intervention_id = i.id
                                 WHERE i.user_id = :user_id");
        $query->execute(['user_id' => $userId]);

        // Delete from `taches` table linked to interventions
        $query = $conn->prepare("DELETE t FROM taches t
                                 INNER JOIN interventions i ON t.intervention_id = i.id
                                 WHERE i.user_id = :user_id");
        $query->execute(['user_id' => $userId]);

        // Finally, delete from `interventions` table
        $query = $conn->prepare("DELETE FROM interventions WHERE user_id = :user_id");
        $query->execute(['user_id' => $userId]);

        $conn->commit();
        echo json_encode(["success" => true, "message" => "All user data deleted successfully."]);

    } catch (Exception $e) {
        $conn->rollBack();
        echo json_encode(["success" => false, "message" => "Error deleting data: " . $e->getMessage()]);
    }
}
