<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

use App\Entity\Client;

#[ORM\Entity]
class Vote
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

        #[ORM\ManyToOne(targetEntity: Post::class, inversedBy: "votes")]
    #[ORM\JoinColumn(name: 'post_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Post $post_id;

        #[ORM\ManyToOne(targetEntity: Client::class, inversedBy: "votes")]
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

    public function getPost_id()
    {
        return $this->post_id;
    }

    public function setPost_id($value)
    {
        $this->post_id = $value;
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

    public function getPostId(): ?Post
    {
        return $this->post_id;
    }

    public function setPostId(?Post $post_id): static
    {
        $this->post_id = $post_id;

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
