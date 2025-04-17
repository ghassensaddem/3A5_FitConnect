<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

use App\Entity\Client;
use Doctrine\Common\Collections\Collection;
use App\Entity\Commentaire;

#[ORM\Entity]
class Post
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

        #[ORM\ManyToOne(targetEntity: Client::class, inversedBy: "posts")]
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

    #[ORM\Column(type: "string", length: 1000)]
    private string $image;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
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

    public function getImage()
    {
        return $this->image;
    }

    public function setImage($value)
    {
        $this->image = $value;
    }

    #[ORM\OneToMany(mappedBy: "post_id", targetEntity: Vote::class)]
    private Collection $votes;

        public function getVotes(): Collection
        {
            return $this->votes;
        }
    
        public function addVote(Vote $vote): self
        {
            if (!$this->votes->contains($vote)) {
                $this->votes[] = $vote;
                $vote->setPost_id($this);
            }
    
            return $this;
        }
    
        public function removeVote(Vote $vote): self
        {
            if ($this->votes->removeElement($vote)) {
                // set the owning side to null (unless already changed)
                if ($vote->getPost_id() === $this) {
                    $vote->setPost_id(null);
                }
            }
    
            return $this;
        }

    #[ORM\OneToMany(mappedBy: "post_id", targetEntity: Commentaire::class)]
    private Collection $commentaires;

    public function __construct()
    {
        $this->votes = new ArrayCollection();
        $this->commentaires = new ArrayCollection();
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
     * @return Collection<int, Commentaire>
     */
    public function getCommentaires(): Collection
    {
        return $this->commentaires;
    }

    public function addCommentaire(Commentaire $commentaire): static
    {
        if (!$this->commentaires->contains($commentaire)) {
            $this->commentaires->add($commentaire);
            $commentaire->setPostId($this);
        }

        return $this;
    }

    public function removeCommentaire(Commentaire $commentaire): static
    {
        if ($this->commentaires->removeElement($commentaire)) {
            // set the owning side to null (unless already changed)
            if ($commentaire->getPostId() === $this) {
                $commentaire->setPostId(null);
            }
        }

        return $this;
    }
}
