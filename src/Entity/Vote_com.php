<?php

namespace App\Entity;

use App\Repository\VoteComRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: VoteComRepository::class)]
class VoteCom
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\ManyToOne(targetEntity: Commentaire::class, inversedBy: 'votes')]
    #[ORM\JoinColumn(name: 'comment_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private ?Commentaire $commentaire = null;

    #[ORM\ManyToOne(targetEntity: Client::class)]
    #[ORM\JoinColumn(name: 'client_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private ?Client $client = null;

    #[ORM\Column(type: 'integer')]
    private ?int $voteType = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getCommentaire(): ?Commentaire
    {
        return $this->commentaire;
    }

    public function setCommentaire(?Commentaire $commentaire): static
    {
        $this->commentaire = $commentaire;
        return $this;
    }

    public function getClient(): ?Client
    {
        return $this->client;
    }

    public function setClient(?Client $client): static
    {
        $this->client = $client;
        return $this;
    }

    public function getVoteType(): ?int
    {
        return $this->voteType;
    }

    public function setVoteType(int $voteType): static
    {
        if (!in_array($voteType, [1, -1, 0], true)) {
            throw new \InvalidArgumentException("VoteType doit être 1 (upvote), -1 (downvote) ou 0 (neutre)");
        }
        $this->voteType = $voteType;
        return $this;
    }
}
