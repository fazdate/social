import { TestBed } from '@angular/core/testing';

import { CommentsWithUserDataService } from './comments-with-user-data.service';

describe('CommentsWithUserDataService', () => {
  let service: CommentsWithUserDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommentsWithUserDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
