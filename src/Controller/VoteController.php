<?php

namespace App\Controller;

use App\Entity\Post;
use App\Entity\Vote;
use App\Entity\Client;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\Routing\Annotation\Route;

class VoteController extends AbstractController
{
    #[Route('/post/{id}/vote/{type}', name: 'app_post_vote')]
    public function vote(int $id, int $type, EntityManagerInterface $em): RedirectResponse
    {
        // Vérification du type de vote (1 = upvote, -1 = downvote)
        if (!in_array($type, [1, -1], true)) {
            throw $this->createNotFoundException('Type de vote invalide.');
        }

        // Récupérer le post
        $post = $em->getRepository(Post::class)->find($id);
        if (!$post) {
            throw $this->createNotFoundException('Post non trouvé.');
        }

        // Récupérer le client (temporairement en dur avec l'ID 8)
        $client = $em->getRepository(Client::class)->find(8);
        if (!$client) {
            throw $this->createNotFoundException('Client non trouvé.');
        }

        // Vérifier si un vote existe déjà pour ce post et ce client
        $existingVote = $em->getRepository(Vote::class)->findOneBy([
            'post' => $post,
            'client' => $client
        ]);

        if ($existingVote) {
            if ($existingVote->getVoteType() === $type) {
                // Même vote : suppression (annulation)
                $em->remove($existingVote);
            } else {
                // Autre type de vote : on change le vote
                $existingVote->setVoteType($type);
            }
        } else {
            // Nouveau vote
            $vote = new Vote();
            $vote->setPost($post);
            $vote->setClient($client);
            $vote->setVoteType($type);
            $em->persist($vote);
        }

        // Sauvegarde en BDD
        $em->flush();

        // Redirection après le vote
        return $this->redirectToRoute('app_listPosts_front');
    }
}
