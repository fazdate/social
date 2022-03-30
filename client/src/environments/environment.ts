// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  firebase: {
    projectId: 'social-2ccfd',
    appId: '1:945868348717:web:8bcde264442e34554f47a4',
    storageBucket: 'social-2ccfd.appspot.com',
    locationId: 'europe-west',
    apiKey: 'AIzaSyDCwJv4o25RH6UFKdnwsHSaRqK71pTmNkE',
    authDomain: 'social-2ccfd.firebaseapp.com',
    messagingSenderId: '945868348717',
  },
  production: false
};

export const baseUrl = 'http://localhost:8080/'

export const urls = {
  createUserUrl: baseUrl + 'createUser',
  changePasswordUrl: baseUrl + 'changePassword',
  changeEmailUrl: baseUrl + 'changeEmail',
  addCommentUrl: baseUrl + 'addComment',
  generateCommentIdUrl: baseUrl + 'generateCommentId',
  getCommentUrl: baseUrl + 'getComment',
  deleteCommentUrl: baseUrl + 'deleteComment',
  getEveryCommentOfPostUrl: baseUrl + 'getEveryCommentOfPost',
  getCommentWithUserDataUrl: baseUrl + 'getCommentWithUserData',
  getMessageUrl: baseUrl + 'getMessage',
  getMessagesListUrl: baseUrl + 'getMessagesListId',
  getEveryMessageFromMessagesListUrl: baseUrl + 'getEveryMessageFromMessagesList',
  sendMessageUrl: baseUrl + 'sendMessage',
  generateMessageIdUrl: baseUrl + 'generateMessageId',
  getMessagesListWIthUserDataUrl: baseUrl + 'getMessagesListWIthUserData',
  getUsersEveryMessagesListWithUserDatUrl: baseUrl + 'getUsersEveryMessagesListWithUserData',
  getPostUserCommentsArrayUrl: baseUrl + 'getPostUserCommentsArray',
  getOwnPostUserCommentsUrl: baseUrl + 'getOwnPostUserComments',
  getFollowedUsersPostUserCommentsUrl: baseUrl + 'getFollowedUsersPostUserComments',
  getOwnAndFollowedUsersPostUserCommentsUrl: baseUrl + 'getOwnAndFollowedUsersPostUserComments',
  getPostUserCommentUrl: baseUrl + 'getPostUserComment',
  createPostUrl: baseUrl + 'createPost',
  generatePostIdUrl: baseUrl + 'generatePostId',
  likeOrUnlikePostUrl: baseUrl + 'likeOrUnlikePost',
  updateUserUrl: baseUrl + 'updateUser',
  getUserUrl: baseUrl + 'getUser',
  followOrUnfollowUserUrl: baseUrl + 'followOrUnfollowUser',
  getEveryUsernameUrl: baseUrl + 'getEveryUsername',
  getDisplayNameFromUsernameUrl: baseUrl + 'getDisplayNameFromUsername',
  getMessagesListIdBetweenUsersUrl: baseUrl + 'getMessagesListIdBetweenUsers'
}



/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
