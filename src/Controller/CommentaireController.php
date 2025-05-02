<?php

namespace App\Controller;

use App\Entity\Commentaire;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\Request;
use App\Form\CommentaireType;



class CommentaireController extends AbstractController
{
    #[Route('/commentaire/delete/{id}', name: 'app_commentaire_delete')]
    public function delete(ManagerRegistry $doctrine, int $id): Response
    {
        $entityManager = $doctrine->getManager();
        $commentaire = $entityManager->getRepository(Commentaire::class)->find($id);

        if (!$commentaire) {
            throw $this->createNotFoundException('Commentaire non trouvé.');
        }

        // On récupère l'id du post pour rediriger après suppression
        $postId = $commentaire->getPost()->getId();

        $entityManager->remove($commentaire);
        $entityManager->flush();

        $this->addFlash('success', 'Commentaire supprimé avec succès.');

        return $this->redirectToRoute('app_post_comments', ['id' => $postId]);
    }

    #[Route('/comment/delete/{id}/front', name: 'app_comment_delete')]
public function delete_com_front(int $id, EntityManagerInterface $em): Response
{
    $comment = $em->getRepository(Commentaire::class)->find($id);

    if (!$comment) {
        throw $this->createNotFoundException('Commentaire non trouvé.');
    }

    $postId = $comment->getPost()->getId(); // pour redirection

    $em->remove($comment);
    $em->flush();

    return $this->redirectToRoute('app_post_comments_front', ['id' => $postId]);
}


#[Route('/comment/edit/{id}', name: 'app_comment_edit')]
public function edit(int $id, Request $request, EntityManagerInterface $em): Response
{
    // Récupérer le commentaire à modifier
    $comment = $em->getRepository(Commentaire::class)->find($id);

    if (!$comment) {
        throw $this->createNotFoundException('Le commentaire avec l\'id ' . $id . ' n\'existe pas.');
    }

    // Créer le formulaire avec les données du commentaire existant
    $form = $this->createForm(CommentaireType::class, $comment);

    // Gérer la soumission du formulaire
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        // Enregistrer les modifications
        $em->flush();

        // Rediriger vers les commentaires du post concerné
        return $this->redirectToRoute('app_post_comments_front', [
            'id' => $comment->getPost()->getId(),
        ]);
    }

    // Rendre le formulaire dans la vue
    return $this->render('commentaire/modifier_commentaire.html.twig', [
        'form' => $form->createView(),
        'comment' => $comment,
    ]);
}




    
}