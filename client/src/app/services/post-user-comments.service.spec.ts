import { TestBed } from '@angular/core/testing';

import { PostUserCommentsService } from './post-user-comments.service';

describe('PostUserCommentsService', () => {
  let service: PostUserCommentsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostUserCommentsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
