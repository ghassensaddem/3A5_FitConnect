<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

use App\Entity\Client;
use Doctrine\Common\Collections\Collection;
use App\Entity\Vote_com;

#[ORM\Entity]
class Commentaire
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

        #[ORM\ManyToOne(targetEntity: Post::class, inversedBy: "commentaires")]
    #[ORM\JoinColumn(name: 'post_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Post $post_id;

        #[ORM\ManyToOne(targetEntity: Client::class, inversedBy: "commentaires")]
    #[ORM\JoinColumn(name: 'client_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Client $client_id;

    #[ORM\Column(type: "date")]
    private \DateTimeInterface $date;

    #[ORM\Column(type: "string", length: 250)]
    private string $author;

    #[ORM\Column(type: "string", length: 1000)]
    private string $contenu;

    #[ORM\Column(type: "integer")]
    private int $upvotes;

    #[ORM\Column(type: "integer")]
    private int $downvotes;

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

    public function getDate()
    {
        return $this->date;
    }

    public function setDate($value)
    {
        $this->date = $value;
    }

    public function getAuthor()
    {
        return $this->author;
    }

    public function setAuthor($value)
    {
        $this->author = $value;
    }

    public function getContenu()
    {
        return $this->contenu;
    }

    public function setContenu($value)
    {
        $this->contenu = $value;
    }

    public function getUpvotes()
    {
        return $this->upvotes;
    }

    public function setUpvotes($value)
    {
        $this->upvotes = $value;
    }

    public function getDownvotes()
    {
        return $this->downvotes;
    }

    public function setDownvotes($value)
    {
        $this->downvotes = $value;
    }

    #[ORM\OneToMany(mappedBy: "comment_id", targetEntity: Vote_com::class)]
    private Collection $vote_coms;

    public function __construct()
    {
        $this->vote_coms = new ArrayCollection();
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

    /**
     * @return Collection<int, Vote_com>
     */
    public function getVoteComs(): Collection
    {
        return $this->vote_coms;
    }

    public function addVoteCom(Vote_com $voteCom): static
    {
        if (!$this->vote_coms->contains($voteCom)) {
            $this->vote_coms->add($voteCom);
            $voteCom->setCommentId($this);
        }

        return $this;
    }

    public function removeVoteCom(Vote_com $voteCom): static
    {
        if ($this->vote_coms->removeElement($voteCom)) {
            // set the owning side to null (unless already changed)
            if ($voteCom->getCommentId() === $this) {
                $voteCom->setCommentId(null);
            }
        }

        return $this;
    }
}
