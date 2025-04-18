<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\TypeactiviteRepository;

#[ORM\Entity(repositoryClass: TypeactiviteRepository::class)]
#[ORM\Table(name: 'typeactivite')]
class Typeactivite
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function setId(int $id): self
    {
        $this->id = $id;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $title = null;

    public function getTitle(): ?string
    {
        return $this->title;
    }

    public function setTitle(string $title): self
    {
        $this->title = $title;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $description = null;

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): self
    {
        $this->description = $description;
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Activiteevent::class, mappedBy: 'typeactivite')]
    private Collection $activiteevents;

    public function __construct()
    {
        $this->activiteevents = new ArrayCollection();
    }

    /**
     * @return Collection<int, Activiteevent>
     */
    public function getActiviteevents(): Collection
    {
        if (!$this->activiteevents instanceof Collection) {
            $this->activiteevents = new ArrayCollection();
        }
        return $this->activiteevents;
    }

    public function addActiviteevent(Activiteevent $activiteevent): self
    {
        if (!$this->getActiviteevents()->contains($activiteevent)) {
            $this->getActiviteevents()->add($activiteevent);
        }
        return $this;
    }

    public function removeActiviteevent(Activiteevent $activiteevent): self
    {
        $this->getActiviteevents()->removeElement($activiteevent);
        return $this;
    }

}
