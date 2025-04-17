<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

use App\Entity\Client;

#[ORM\Entity]
class Vote_com
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

        #[ORM\ManyToOne(targetEntity: Commentaire::class, inversedBy: "vote_coms")]
    #[ORM\JoinColumn(name: 'comment_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Commentaire $comment_id;

        #[ORM\ManyToOne(targetEntity: Client::class, inversedBy: "vote_coms")]
    #[ORM\JoinColumn(name: 'client_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Client $client_id;

    #[ORM\Column(type: "boolean")]
    private bool $vote_type;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getComment_id()
    {
        return $this->comment_id;
    }

    public function setComment_id($value)
    {
        $this->comment_id = $value;
    }

    public function getClient_id()
    {
        return $this->client_id;
    }

    public function setClient_id($value)
    {
        $this->client_id = $value;
    }

    public function getVote_type()
    {
        return $this->vote_type;
    }

    public function setVote_type($value)
    {
        $this->vote_type = $value;
    }

    public function isVoteType(): ?bool
    {
        return $this->vote_type;
    }

    public function setVoteType(bool $vote_type): static
    {
        $this->vote_type = $vote_type;

        return $this;
    }

    public function getCommentId(): ?Commentaire
    {
        return $this->comment_id;
    }

    public function setCommentId(?Commentaire $comment_id): static
    {
        $this->comment_id = $comment_id;

        return $this;
    }

    public function getClientId(): ?Client
    {
        return $this->client_id;
    }

    public function setClientId(?Client $client_id): static
    {
        $this->client_id = $client_id;

        return $this;
    }
}
