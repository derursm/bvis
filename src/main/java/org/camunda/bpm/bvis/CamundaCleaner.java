package org.camunda.bpm.bvis;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

@Startup
@Singleton
public class CamundaCleaner {

	protected RepositoryService repositoryService;
	protected RuntimeService runtimeService;
	protected IdentityService identityService;
	protected FilterService filterService;
	protected AuthorizationService authorizationService;

	public void clean(ProcessEngine engine) {
		
		repositoryService = engine.getRepositoryService();
		runtimeService = engine.getRuntimeService();
		identityService = engine.getIdentityService();
		filterService = engine.getFilterService();
		authorizationService = engine.getAuthorizationService();

		// Delete all previous data in camunda
		cleanInstances();
		cleanMemberships();
		cleanUsers();
		cleanGroups();
		cleanFilters();
		cleanAuthorizations();
	}

	private void cleanInstances() {
		List<ProcessDefinition> definitions = repositoryService
				.createProcessDefinitionQuery().list();

		for (ProcessDefinition definition : definitions) {
			List<ProcessInstance> instances = runtimeService
					.createProcessInstanceQuery()
					.processDefinitionId(definition.getId()).list();
			for (ProcessInstance instance : instances) {
				runtimeService.deleteProcessInstance(instance.getId(), null);
				System.out.println("Deleted instance " + instance.getId());
			}
		}
	}

	private void cleanMemberships() {
		identityService.deleteMembership("admin", Groups.CAMUNDA_ADMIN);

		identityService.deleteMembership("urs", "bvis");
		identityService.deleteMembership("marcus", "bvis");
		identityService.deleteMembership("alan", "bvis");
		identityService.deleteMembership("oliver", "bvis");
		identityService.deleteMembership("vit", "bvis");
		identityService.deleteMembership("lena", "bvis");
		identityService.deleteMembership("alex", "bvis");
		
		identityService.deleteMembership("urs", "clerks-contract");
		identityService.deleteMembership("marcus", "clerks-contract");
		identityService.deleteMembership("alan", "clerks-contract");
		
		identityService.deleteMembership("oliver", "clerks-claims");
		identityService.deleteMembership("vit", "clerks-claims");

		identityService.deleteMembership("lena", "management-contract");

		identityService.deleteMembership("alex", "management-claims");
	}

	private void cleanUsers() {
		identityService.deleteUser("admin");
		identityService.deleteUser("urs");
		identityService.deleteUser("marcus");
		identityService.deleteUser("alan");
		identityService.deleteUser("oliver");
		identityService.deleteUser("vit");
		identityService.deleteUser("lena");
		identityService.deleteUser("alex");
	}

	private void cleanGroups() {
		identityService.deleteGroup("bvis");
		identityService.deleteGroup("clerks-contract");
		identityService.deleteGroup("management-contract");
		identityService.deleteGroup("clerks-claims");
		identityService.deleteGroup("management-claims");
	}

	private void cleanFilters() {
		List<Filter> filters = filterService.createFilterQuery().list();
		for (Filter f : filters) {
			filterService.deleteFilter(f.getId());
		}
	}

	private void cleanAuthorizations() {
		List<Authorization> auths = authorizationService
				.createAuthorizationQuery().list();
		for (Authorization a : auths) {
			authorizationService.deleteAuthorization(a.getId());
		}
	}
}
